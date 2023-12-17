import './App.css';
import MediaDisplay from './elements/MediaDisplay';
import {useEffect, useState} from 'react';
import axios from 'axios';
import Media from './types/media';

function App() {
    const [mediaList, setMediaList]: [Media[], any] = useState([]);

    async function refreshMediaList() {
        try {
            const response = await axios.get(`media`);
            const mediaList = response.data;
            setMediaList(mediaList);
        } catch (e) {
            alert(e);
            return [];
        }
    }

    useEffect(() => {
        refreshMediaList();
    }, []);

    return (
        <>
            <h1>Puck Configuration Client</h1>
            <div className={'row'}>
                <MediaDisplay mediaList={mediaList}/>
                <div className={'column'} id={'toolbar'}>
                    <div>
                        <button onClick={refreshMediaList}>Refresh Media List</button>
                        <div>
                            <button>Scan Media Storage Folder For Unadded Media</button>
                        </div>
                        <div>
                            <form action="/media" method="put">
                                <label>
                                    Name:
                                    <input type={'text'}/>
                                </label>
                                <label>
                                    File:
                                    <input type={'file'}/>
                                </label>
                                <input type={'submit'} value={'Add File to Media'}/>
                            </form>
                        </div>
                    </div>
                    <div>
                        <div>
                            <button>Set Password</button>
                            <input type={'password'}/>
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
