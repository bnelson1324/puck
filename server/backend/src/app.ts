import express from 'express';
import path from 'path';
import bodyParser from 'body-parser';
import {config, loadConfig} from './data/config';
import {loadMediaStoragePath} from './data/mediaDatabase';
import {router as mediaRouter} from './routes/media';
import {router as passwordRouter} from './routes/password';
import {router as loginRouter} from './routes/login';


async function start(): Promise<void> {
    loadConfig(path.join(__dirname, '../config.json'));
    console.log('Config loaded');

    await loadMediaStoragePath(path.isAbsolute(config.mediaStoragePath) ?
        config.mediaStoragePath :
        path.join(__dirname, config.mediaStoragePath)
    );
    console.log(`Media storage loaded at ${config.mediaStoragePath}`);

    const app = express();
    app.use(bodyParser.json()); // support json encoded bodies
    app.use(express.urlencoded({extended: true})); // support encoded bodies


    app.use('/media', mediaRouter);
    app.use('/password', passwordRouter);
    app.use('/login', loginRouter);

    app.listen(config.port);
    console.log(`Server ready, listening on port ${config.port}...`);
}

start();
