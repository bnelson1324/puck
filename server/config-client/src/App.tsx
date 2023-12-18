import './App.css';
import MediaDisplay from './elements/MediaDisplay';
import {useEffect, useState} from 'react';
import axios from 'axios';
import Media from './types/media';
import UploadMediaForm from './elements/UploadMediaForm';
import RequestButton from './elements/RequestButton';

function App() {
    async function refreshMediaList() {
        try {
            const response = await axios.get(`media`);
            const mediaList = response.data;
            setMediaList(mediaList);
        } catch (e) {
            alert(e);
        }
    }

    async function removeMedia(id: number) {
        try {
            await axios.delete(`media/${id}`);
            await refreshMediaList();
        } catch (e) {
            alert(e);
        }
    }

    const [mediaList, setMediaList]: [Media[], any] = useState([]);

    useEffect(() => {
        refreshMediaList();
    }, []);

    return (
        <>
            <h1>Puck Configuration Client</h1>
            <div className={'row'}>
                <MediaDisplay mediaList={mediaList} deleteMedia={removeMedia}/>
                <div className={'column'} id={'toolbar'}>
                    <div>
                        <button onClick={refreshMediaList}>Refresh Media List</button>
                        <div>
                            <button>Scan Media Storage Folder For Unadded Media</button>
                        </div>
                        <div>
                            <UploadMediaForm refreshMedia={refreshMediaList}/>
                        </div>
                    </div>
                    <div>
                        <div>
                            <RequestButton buttonText={'Set Password'} action={'PUT'} url={'/password'}
                                           inputType={'password'} inputName={'password'}/>
                        </div>

                        <div>
                            <button>Set Media Storage Folder</button>
                            <input type={'text'}/>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default App;
