from pathlib import Path
import subprocess
import os
import shutil

# get paths
serverDir = Path(__file__).parent
backendDir = os.path.join(serverDir, 'backend')
configClientDir = os.path.join(serverDir, 'config-client')

# build backend
backendBuild = subprocess.Popen('tsc --build', cwd=backendDir, shell=True)
shutil.copy(
    os.path.join(backendDir, 'src/data/mediaSchema.sql'),
    os.path.join(backendDir, 'build/src/data/mediaSchema.sql')
)

# build config client
configClientBuild = subprocess.Popen('yarn build', cwd=configClientDir, shell=True)

# wait for both builds to finish
backendBuild.wait()
configClientBuild.wait()

# move files to build directory
# TODO
