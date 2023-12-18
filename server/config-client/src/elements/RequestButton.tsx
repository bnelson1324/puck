import axios from 'axios';
import {useState} from 'react';

export default function RequestButton(props: {
    buttonText: string
    action: string,
    url: string,
    inputType: string,
    inputName: string,
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
        alert('Successful request')
    }

    const [inputVal, setInputVal]: [string, any] = useState('');

    return (
        <>
            <button onClick={() => sendRequest()}>{props.buttonText}</button>
            <input type={props.inputType} value={inputVal} onChange={(e) => setInputVal(e.target.value)}/>
        </>
    );
}
