import express from 'express';
import {authenticate} from '../data/auth';
import {config, writeConfig} from '../data/config';
import {loadMediaStoragePath} from '../data/mediaDatabase';


const router = express.Router();
router.use(authenticate);

router.route('/')
    .get((req: any, res: any) => {
        res.send(config.mediaStoragePath);
    })
    .put(async (req: any, res: any) => {
        config.mediaStoragePath = req.body.mediaStoragePath;
        writeConfig();
        await loadMediaStoragePath(config.mediaStoragePath);
        res.send('Media storage path set');
    });

export {router};
