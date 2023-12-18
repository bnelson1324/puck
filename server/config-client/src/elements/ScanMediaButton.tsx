import axios from 'axios';

export default function ScanMediaButton(props: { refreshMedia: () => void }) {
    async function scanMedia() {
        const response = await axios.put('/media/scan');
        const newMedia: string[] = response.data;
        if (newMedia.length == 0) {
            alert('No new media found');
            return;
        }

        const requests: Promise<any>[] = [];
        for (const fileName of newMedia) {
            // prompt user to name the media
            const name = prompt(`Please enter a name for file: ${fileName}`);
            if (name == null)
                continue;

            // add media to db with filename
            const options = {
                method: 'PUT',
                url: `/media/add/${fileName}`,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: {name: name}
            };
            requests.push(axios.request(options).catch(
                function (error) {
                    alert(error.message);
                }
            ));
        }
        await Promise.all(requests);
        props.refreshMedia();
    }

    return (
        <>
            <button onClick={scanMedia}>Scan Media Storage Folder For Unadded Media</button>
        </>
    );
}
