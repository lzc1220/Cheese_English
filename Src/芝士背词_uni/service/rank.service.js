import wRequest from './index';
export function getRank() {
    return wRequest.post('/user/rank', {
        start: 0,
        end: -1
    });
}
