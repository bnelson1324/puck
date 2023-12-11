import express from 'express';
import {comparePassword} from './password';
import {authenticateLocalhost, generateJWT} from '../data/auth';
import {config, writeConfig} from '../data/config';

const router = express.Router();

router.route('/')
    .post(async (req: any, res: any) => {
        // log in
        if (await comparePassword(req.body.password)) {
            res.status(200).json(generateJWT());
        } else {
            res.status(401).send('Incorrect password');
        }
    })
    .delete(authenticateLocalhost, async (req, res) => {
        // log out
        logoutAllUsers();
    });

function logoutAllUsers() {
    config.sessionReset += 1;
    writeConfig();
}

export {router, logoutAllUsers};
