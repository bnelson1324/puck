import fs from 'fs';
import path from 'path';
import * as os from 'os';


let config: {
    port: number,
    mediaStoragePath: string,
};

function loadConfig(path: string): void {
    let configFile: string;
    if (!fs.existsSync(path)) {
        // copy defaultConfig to path if a config doesn't already exist
        console.log('No config found, creating one with default settings...');
        config = defaultConfig;
        writeConfig(path);
    } else {
        configFile = fs.readFileSync(path, 'utf8');
        config = JSON.parse(configFile);
    }
}

function writeConfig(path: string): void {
    fs.writeFileSync(path, JSON.stringify(config), {flag: 'w', encoding: 'utf8'});
}

const defaultConfig: typeof config = {
    'port': 9998,
    'mediaStoragePath': path.join(os.homedir(), './puckMediaStorage'),
};

export {config, loadConfig, writeConfig};
