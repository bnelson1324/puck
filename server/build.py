from pathlib import Path
import subprocess
import os
import shutil

# get paths
serverDir = Path(__file__).parent
backendDir = os.path.join(serverDir, 'backend')
configClientDir = os.path.join(serverDir, 'config-client')
buildDir = os.path.join(serverDir, 'build')

# clear build directory
if os.path.exists(buildDir) and os.path.isdir(buildDir):
    shutil.rmtree(buildDir)

# build backend
backendBuild = subprocess.Popen('tsc --build', cwd=backendDir, shell=True)

schemaTargetDir = os.path.join(backendDir, 'build', 'src', 'data')
if not os.path.exists(schemaTargetDir):
    os.makedirs(schemaTargetDir)
shutil.copy(
    os.path.join(backendDir, 'src', 'data', 'mediaSchema.sql'),
    os.path.join(schemaTargetDir, 'mediaSchema.sql')
)

configPath = os.path.join(backendDir, 'build', 'config.json')
if os.path.exists(configPath):
    os.remove(configPath)

# build config client
subprocess.Popen('yarn install', cwd=configClientDir, shell=True).wait()
configClientBuild = subprocess.Popen('yarn build', cwd=configClientDir, shell=True)

# wait for both builds to finish
backendBuild.wait()
configClientBuild.wait()

# move built files to build directory
shutil.move(os.path.join(backendDir, 'build'), buildDir)

configClientServeDir = os.path.join(buildDir, 'static')
shutil.move(os.path.join(configClientDir, 'build'), configClientServeDir)

# move scripts to static folder
shutil.copytree(
    os.path.join(configClientServeDir, 'static'),
    os.path.join(buildDir, 'static'),
    dirs_exist_ok=True
)

# copy package.json, then install dependencies
shutil.copy(
    os.path.join(backendDir, 'package.json'),
    os.path.join(buildDir, 'package.json')
)
subprocess.Popen('yarn install', cwd=buildDir, shell=True).wait()
