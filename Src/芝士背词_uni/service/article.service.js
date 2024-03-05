import wRequest from './index';
export function getArticle() {
    return wRequest.post('/es/random_text', {
        num: 0
    });
}
export function getSentence() {
    return wRequest.post('/es/random_sentence', {
        num: 0
    });
}
export function createComment(param) {
    return wRequest.post('/es/comments_on_text', param);
}
export function fetchComment(param) {
    return wRequest.post('/es/text_comments', param);
}
