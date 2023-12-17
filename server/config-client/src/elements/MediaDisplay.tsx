import Media from '../types/media';

export default function MediaDisplay(props: { mediaList: Media[], deleteMedia: (id: number) => void }) {
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
                <tbody key={media.id}>
                <tr>
                    <td>{media.id}</td>
                    <td>{media.name}</td>
                    <td>{media.fileName}</td>
                    <td>
                        <button onClick={() => props.deleteMedia(media.id)}>X</button>
                    </td>
                </tr>
                </tbody>
            ))}
        </table>
    );
}
