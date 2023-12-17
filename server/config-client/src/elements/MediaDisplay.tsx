import Media from '../types/media';

export default function MediaDisplay(props: { mediaList: Media[] }) {
    return (
        <table id={'mediaDisplay'}>
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>File Name</th>
            </tr>
            </thead>
            {props.mediaList.map(media => (
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
