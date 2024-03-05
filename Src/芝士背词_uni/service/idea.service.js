import wRequest from './index';
export function loadIdea(param) {
    return wRequest.post('/user/random_dynamic', param);
}
export function postIdea(url, param) {
    return wRequest.post(url, param);
}
export function forkIdea(param) {
    return wRequest.post('/user/likes_dynamic', param);
}
