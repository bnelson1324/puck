import express from 'express';
import path from 'path';
import {loadMediaStoragePath} from './data/database';
import {config, loadConfig} from './data/config';
import media from './routes/media';


async function start(): Promise<void> {
    loadConfig(path.join(__dirname, '../config.json'));
    console.log('Config loaded');

    await loadMediaStoragePath(path.isAbsolute(config.mediaStoragePath) ?
        config.mediaStoragePath :
        path.join(__dirname, config.mediaStoragePath)
    );
    console.log(`Media storage loaded at ${config.mediaStoragePath}`);

    const app = express();
    app.use('/media', media);

    app.listen(config.port);
    console.log(`Server ready, listening on port ${config.port}...`);
}

start();
