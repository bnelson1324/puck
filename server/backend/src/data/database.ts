import path from 'path';
import sqlite3 from 'sqlite3';
import {Database, open} from 'sqlite';
import fs from 'fs';


let db: Database;

async function loadMediaStoragePath(mediaStoragePath: string): Promise<void> {
    // if mediaStoragePath doesn't exist, then create it
    if (!fs.existsSync(mediaStoragePath)) {
        fs.mkdirSync(mediaStoragePath);
    }

    // load db
    db = await open({
        filename: path.join(mediaStoragePath, 'puck-db.sqlite'),
        driver: sqlite3.Database,
    });
    const schema = fs.readFileSync(path.join(__dirname, 'schema.sql'), 'utf8');
    await db.exec(schema);
}

interface Media {
    id: number,
    fileName: string,
    name: string,
    timeAdded: number
}

async function fetchMediaList(): Promise<Media[]> {
    return await db.all(`
        SELECT id, fileName, name
        FROM media
    `);
}

async function fetchMedia(id: number): Promise<Media> {
    const res = await db.get(`
        SELECT id, fileName, name
        FROM media
        WHERE id == ?
    `, id);
    if (res == undefined)
        throw new Error(`Media not found with ID ${id}`);

    return res;
}

async function fetchMediaWithFileName(fileName: string): Promise<Media> {
    const res = await db.get(`
        SELECT id, fileName, name
        FROM media
        WHERE fileName == ?
    `, fileName);
    if (res == undefined)
        throw new Error(`Media not found with file name: ${fileName}`);

    return res;
}

async function mediaWithFilenameExists(fileName: string): Promise<boolean> {
    const res = await db.get(`
        SELECT id, fileName, name
        FROM media
        WHERE fileName == ?
    `, fileName);
    return res != undefined;
}


async function addMediaWithFilename(fileName: string, name: string): Promise<void> {
    await db.run(`
        INSERT INTO media (fileName, name, timeAdded)
        VALUES (?, ?, UNIXEPOCH())
    `, fileName, name);
}


export {
    loadMediaStoragePath,
    fetchMediaList,
    fetchMedia,
    fetchMediaWithFileName,
    mediaWithFilenameExists,
    addMediaWithFilename
};
