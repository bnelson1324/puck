import './App.css';
import MediaDisplay from './elements/MediaDisplay';

function App() {
    return (
        <>
            <h1>Puck Configuration Client</h1>
            <div className={'row'}>
                <MediaDisplay/>
                <div className={'column'} id={'toolbar'}>
                    <div>
                        <button>Refresh Media Display</button>
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
    )
        ;
}

export default App;
