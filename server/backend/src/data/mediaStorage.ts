import fs, {PathLike} from 'fs';
import formidable from 'formidable';
import {deleteMediaEntry, fetchMedia, mediaWithFilenameExists} from './mediaDatabase';
import path from 'path';
import {config} from './config';


function downloadMediaFile(req: any, res: any, next: any, directory: string,
                           callback: (fileName: string, filePath: PathLike, mediaName: string) => void) {
    // download file from request
    const form = formidable({
        uploadDir: directory,
        maxFileSize: 5 * 1024 * 1024 * 1024, // 5gb
    });
    form.parse(req, async (err, fields, files) => {
        if (err) {
            return next(err);
        }

        if (!fields.name) {
            return next(new Error('Request does not provide name'));
        }

        if (!files.file || files.file.length == 0) {
            return next(new Error('Request does not provide file'));
        }

        const file = files.file[0];
        const fileName = file.originalFilename ?? file.newFilename;

        callback(fileName, file.filepath, fields.name[0]);
    });
}

async function deleteMedia(id: number) {
    const media = await fetchMedia(id);
    await deleteMediaEntry(id);
    fs.rmSync(getMediaAbsolutePath(media.fileName));
}

function getMediaAbsolutePath(fileName: string) {
    return path.join(config.mediaStoragePath, fileName);
}

async function scanForNewMedia(mediaStoragePath: string): Promise<string[]> {
    const res: string[] = [];
    for (const fileName of fs.readdirSync(mediaStoragePath)) {
        if (!fileName.endsWith('.sqlite') && !await mediaWithFilenameExists(fileName)) {
            res.push(fileName);
        }
    }
    return res;
}

export {
    downloadMediaFile,
    deleteMedia,
    getMediaAbsolutePath,
    scanForNewMedia
};
