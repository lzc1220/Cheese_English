import wRequest from './index';
export function fetchWord() {
    return wRequest.post(`/word/random`, {
        wordContent: 'randomWD',
        idx: -1
    });
}
export function fetchForkWords(openId) {
    return wRequest.post(`/word/review`, {
        wordContent: 'collectedWD',
        idx: -1,
        openId
    });
}
export function addFork(param) {
    return wRequest.post(`/word/collect`, param);
}
export function cancelFork(param) {
    return wRequest.post('/word/delete', param);
}
