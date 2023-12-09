import fs from 'fs';
import path from 'path';
import * as os from 'os';
import crypto from 'node:crypto';


let configPath: string;
let config: {
    port: number,
    mediaStoragePath: string,
    jwtSecretKey: string,
    hashedPassword: string,
    sessionReset: number,
};

function loadConfig(path: string): void {
    configPath = path;
    if (!fs.existsSync(configPath)) {
        // copy defaultConfig to path if a config doesn't already exist
        console.log('No config found, creating one with default settings...');
        config = defaultConfig;
        writeConfig();
    } else {
        const configFile = fs.readFileSync(configPath, 'utf8');
        config = JSON.parse(configFile);
    }
}

function writeConfig(): void {
    if (configPath == null)
        throw new Error('Config path is not specified, cannot write')
    fs.writeFileSync(configPath, JSON.stringify(config), {flag: 'w', encoding: 'utf8'});
}

const defaultConfig: typeof config = {
    'port': 9998,
    'mediaStoragePath': path.join(os.homedir(), './puckMediaStorage'),
    'jwtSecretKey': crypto.randomBytes(64).toString(),
    'hashedPassword': '',
    'sessionReset': 0,
};

export {config, loadConfig, writeConfig};
