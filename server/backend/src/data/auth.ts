import jwt from 'jsonwebtoken';
import {config} from './config';
import {NextFunction} from 'express';

function generateJWT(): string {
    return jwt.sign({sessionReset: config.sessionReset}, config.jwtSecretKey);
}

function authenticateLocalhost(req: any, res: any, next: NextFunction) {
    if (!req.ip?.endsWith('127.0.0.1')) {
        next(new Error('Unauthorized'));
        return res.status(401).send('Unauthorized');
    }

    next();
}

function authenticate(req: any, res: any, next: NextFunction) {
    // check if ip is localaddress
    if (req.ip?.endsWith('127.0.0.1'))
        return next();

    // check auth header
    const authHeader = req.headers.authorization;
    if (authHeader == null)
        return res.sendStatus(401);

    jwt.verify(authHeader.substring(1, authHeader.length - 1), config.jwtSecretKey, (err: any, payload: any) => {
        if (err) {
            res.sendStatus(401);
            return next(err);
        }

        if (payload.sessionReset !== config.sessionReset) {
            res.sendStatus(401);
            return;
        }

        next();
    });
}

export {
    generateJWT,
    authenticateLocalhost,
    authenticate,
};
