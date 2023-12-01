import fs, {PathLike} from 'fs';
import formidable from 'formidable';
import {fetchMediaWithFileName} from './database';
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
            next(err);
            return;
        }

        if (!fields.name) {
            next(new Error('Request does not provide name'));
            return;
        }

        if (!files.file || files.file.length == 0) {
            next(new Error('Request does not provide file'));
            return;
        }

        const file = files.file[0];
        const fileName = file.originalFilename ?? file.newFilename;

        callback(fileName, file.filepath, fields.name[0]);
    });
}

function getMediaAbsolutePath(fileName: string) {
    return path.join(config.mediaStoragePath, fileName);
}

async function scanForNewMedia(mediaStoragePath: string): Promise<string[]> {
    return fs.readdirSync(mediaStoragePath)
        .filter(path => !path.endsWith('.sqlite'))
        .filter(async path => await fetchMediaWithFileName(path) == undefined);
}

export {downloadMediaFile, getMediaAbsolutePath, scanForNewMedia};
