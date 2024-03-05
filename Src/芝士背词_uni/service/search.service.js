import wRequest from './index';
export function searchWord(param) {
    return wRequest.post(`/word/select`, param);
}
