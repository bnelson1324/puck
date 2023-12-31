import './App.css';
import MediaDisplay from './elements/MediaDisplay';
import {useEffect, useState} from 'react';
import axios from 'axios';
import Media from './types/media';
import UploadMediaForm from './elements/UploadMediaForm';
import RequestButton from './elements/RequestButton';
import ScanMediaButton from './elements/ScanMediaButton';

function App() {
    async function refreshMediaList() {
        const response = await axios.get(`media`)
            .catch(function (error) {
                alert(`${error.message}: ${error.response.data}`);
            });
        if (response != null) {
            const mediaList = response.data;
            setMediaList(mediaList);
        }
    }

    async function removeMedia(id: number) {
        await axios.delete(`media/${id}`)
            .catch(function (error) {
                alert(`${error.message}: ${error.response.data}`);
            });
        await refreshMediaList();
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
                            <ScanMediaButton refreshMedia={refreshMediaList}/>
                        </div>
                        <div>
                            <UploadMediaForm refreshMedia={refreshMediaList}/>
                        </div>
                    </div>
                    <div>
                        <div>
                            <RequestButton buttonText={'Set Password'} action={'PUT'} url={'/password'}
                                           inputType={'password'} inputName={'password'} getDefaultInputVal={null}
                                           refreshMedia={refreshMediaList}/>
                        </div>

                        <div>
                            <RequestButton buttonText={'Set Media Storage Folder'} action={'PUT'}
                                           url={'/mediaStoragePath'} inputType={'text'} inputName={'mediaStoragePath'}
                                           getDefaultInputVal={async () => (await axios.get('/mediaStoragePath')).data}
                                           refreshMedia={refreshMediaList}/>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}

export default App;
