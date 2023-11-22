import express from 'express';
import {addMediaWithFilename, fetchMedia, fetchMediaList, mediaWithFilenameExists} from '../data/database';
import scanForNewMedia from '../data/mediaStorage';
import {config} from '../data/config';


const router = express.Router();

router.route('/')
    .get(async (req: any, res: any) => {
        // send a list of all media
        try {
            res.json(await fetchMediaList());
        } catch (e) {
            console.error(e);
            res.status(500).send(`Error fetching media list`);
        }
    })
    .put(async (req: any, res: any) => {
        // insert media at path into database
        try {
            if (await mediaWithFilenameExists(req.query.fileName)) {
                await addMediaWithFilename(req.query.fileName, req.query.name);
                res.status(200);
            } else {
                res.status(400).send(`Error: Media at path ${req.query.fileName} already exists`);
            }
        } catch (e) {
            console.error(e);
            res.status(500).send(`Error inserting media at path: ${req.query.fileName}`);
        }
    });

router.route('/:id')
    .get(async (req: any, res: any) => {
            // send file at id
            try {
                const media = await fetchMedia(req.params.id);
                res.sendFile(media.fileName);
            } catch (e) {
                console.error(e);
                res.status(400).send(`Error finding file with id: ${req.params.id}`);
            }
        }
    );

router.route('/scan')
    .put(async (req: any, res: any) => {
        // return filenames of unadded media
        return await scanForNewMedia(config.mediaStoragePath);
    });

export = router;
