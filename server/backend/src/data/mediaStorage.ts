import fs from 'fs';
import {fetchMediaWithFileName} from './database';


async function scanForNewMedia(mediaStoragePath: string): Promise<string[]> {
    return fs.readdirSync(mediaStoragePath)
        .filter(path => !path.endsWith('.sqlite'))
        .filter(async path => await fetchMediaWithFileName(path) == undefined);
}

export = scanForNewMedia;
