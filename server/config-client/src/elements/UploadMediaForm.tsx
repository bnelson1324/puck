import {useState} from 'react';
import axios from 'axios';


export default function UploadMediaForm(props: { refreshMedia: () => void }) {
    async function uploadMedia() {
        if (mediaName.trim() === '') {
            alert('Please set a name for the media');
            return;
        }

        const formData = new FormData();
        formData.append('name', mediaName);
        formData.append('file', mediaFile[0]);
        await axios.post('/media', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        }).catch(
            function (error) {
                alert(`${error.message}: ${error.response.data}`);
            }
        );
        props.refreshMedia();
    }

    const [mediaName, setMediaName]: [string, any] = useState('');
    const [mediaFile, setMediaFile]: [File[], any] = useState([]);

    return (
        <>
            <label>
                Name:
                <input type={'text'} value={mediaName} onChange={(e) => setMediaName(e.target.value)}/>
            </label>
            <label>
                File:
                <input type={'file'} onChange={(e) => setMediaFile(e.target.files)}/>
            </label>
            <button onClick={uploadMedia}>Upload File to Media</button>
        </>
    );
}
