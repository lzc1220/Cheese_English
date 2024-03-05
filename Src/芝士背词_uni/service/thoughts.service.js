import wRequest from './index';
export function fetchThoughts(param) {
    return wRequest.post('/user/secret', param);
}
