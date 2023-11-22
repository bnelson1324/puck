import {useState} from 'react';
import axios from 'axios';

export default function MediaDisplay() {
    async function getMediaList(): Promise<Media[]> {
        try {
            return await axios.get(`media`);
        } catch (e) {
            alert(e);
            return [];
        }
    }

    const [mediaList, setMediaList] = useState(getMediaList());
    return (
        <table id={'mediaDisplay'}>
        </table>
    );
}

interface Media {
    id: number,
    fileName: string,
    name: string,
    timeAdded: number
}
