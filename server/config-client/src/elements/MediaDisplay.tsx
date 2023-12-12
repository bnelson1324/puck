import {useEffect, useState} from 'react';
import axios from 'axios';

export default function MediaDisplay() {
    async function getMediaList(): Promise<Media[]> {
        try {
            const response = await axios.get(`media`);
            return response.data;
        } catch (e) {
            alert(e);
            return [];
        }
    }

    useEffect(() => {
        const refresh = async () => {
            const mediaList = await getMediaList();
            setMediaList(mediaList);
        };
        refresh();
    });

    const [mediaList, setMediaList]: [Media[], any] = useState([]);

    return (
        <table id={'mediaDisplay'}>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>File Name</th>
            </tr>
            </thead>
            {mediaList.map(media => (
                <tbody>
                <tr>
                    <td>{media.id}</td>
                    <td>{media.name}</td>
                    <td>{media.fileName}</td>
                </tr>
                </tbody>
            ))}
        </table>
    );
}

interface Media {
    id: number,
    fileName: string,
    name: string,
}
