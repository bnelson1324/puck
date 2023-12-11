import express, {NextFunction} from 'express';
import fs from 'fs';
import {addMediaWithFilename, fetchMedia, fetchMediaList, mediaWithFilenameExists} from '../data/mediaDatabase';
import {deleteMedia, downloadMediaFile, getMediaAbsolutePath, scanForNewMedia} from '../data/mediaStorage';
import {config} from '../data/config';
import {authenticate} from '../data/auth';


const router = express.Router();
router.use(authenticate);

router.route('/')
    .get(async (req: any, res: any, next: NextFunction) => {
        // send a list of all media
        try {
            res.json(await fetchMediaList());
        } catch (err) {
            await res.status(500).send(`Error fetching media list`);
            next(err);
        }
    })
    .put(async (req: any, res: any, next: NextFunction) => {
        // download file from request
        try {
            downloadMediaFile(req, res, next, config.mediaStoragePath, async (fileName, oldPath, mediaName) => {
                try {
                    const newPath = getMediaAbsolutePath(fileName);

                    // check if file already exists
                    if (await mediaWithFilenameExists(fileName)) {
                        fs.rmSync(oldPath);
                        await res.status(400).send(`File at ${newPath} already exists in database`);
                        return;
                    }

                    // rename file and run callback
                    fs.renameSync(oldPath, newPath);
                    console.log(`Downloaded new file to ${newPath}`);

                    // insert media at path into database
                    await addMediaWithFilename(fileName, mediaName);
                    res.status(200).send('File successfully uploaded');
                } catch (err) {
                    await res.status(500).send(`Error uploading file to server`);
                    next(err);
                }
            });
        } catch (err) {
            res.status(500).send(`Error uploading file to server`);
        }
    });

router.route('/:id')
    .get(async (req: any, res: any, next: NextFunction) => {
            // send file with id
            try {
                const media = await fetchMedia(req.params.id);
                res.sendFile(getMediaAbsolutePath(media.fileName));
            } catch (err) {
                res.status(400).send(`Error finding file with id: ${req.params.id}`);
            }
        }
    )
    .delete(async (req: any, res: any, next: NextFunction) => {
        // delete file with id
        try {
            await deleteMedia(req.params.id);
            res.send('File deleted successfully');
        } catch (err) {
            res.status(400).send(`Error deleting file with id: ${req.params.id}`);
        }
    });

router.route('/scan')
    .put(async (req: any, res: any) => {
        // return filenames of unadded media
        return await scanForNewMedia(config.mediaStoragePath);
    });

export {router};
