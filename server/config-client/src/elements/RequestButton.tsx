import axios from 'axios';
import {useEffect, useState} from 'react';

export default function RequestButton(props: {
    buttonText: string,
    action: string,
    url: string,
    inputType: string,
    inputName: string,
    getDefaultInputVal: (() => Promise<string>) | null,
    refreshMedia: () => void
}) {
    async function sendRequest() {
        const options = {
            method: props.action,
            url: props.url,
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: {[props.inputName]: inputVal}
        };
        await axios.request(options).catch(
            function (error) {
                alert(error.message);
            }
        );

        props.refreshMedia();
        alert('Successful request');
    }

    const [inputVal, setInputVal]: [string, any] = useState('');

    useEffect(() => {
        async function setDefaultInputVal() {
            if (props.getDefaultInputVal == null)
                return;
            setInputVal(await props.getDefaultInputVal());
        }

        setDefaultInputVal();
    }, []);

    return (
        <>
            <button onClick={() => sendRequest()}>{props.buttonText}</button>
            <input className={'requestButtonInput'} type={props.inputType} value={inputVal}
                   onChange={(e) => setInputVal(e.target.value)}
            />
        </>
    );
}
