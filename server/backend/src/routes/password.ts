import express from 'express';
import bcrypt from 'bcrypt';
import {config, writeConfig} from '../data/config';
import {authenticateLocalhost} from '../data/auth';
import {logoutAllUsers} from './login';

const router = express.Router();

router.route('/')
    .put(authenticateLocalhost, async (req, res) => {
        config.hashedPassword = await hashPassword(req.body.password);
        writeConfig();
        res.status(200).send('Password changed, all active sessions logged out');

        // when password is changed, log everybody out of the server
        logoutAllUsers();
    });

async function hashPassword(password: string): Promise<string> {
    return await bcrypt.hash(password, 10);
}

async function comparePassword(password: string): Promise<boolean> {
    return await bcrypt.compare(password, config.hashedPassword);
}

export {router, hashPassword, comparePassword};
