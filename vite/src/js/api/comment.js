import service from "../utils/request";

export async function getCommentByPostId(postId, page, size, sort) {
    return service.get("/no_auth/post/get_comments", {
        params: {
            id: postId,
            page: page,
            size: size,
            sort: sort
        }
    })
}

export async function likeComment(commentId) {
    return service.put("/comment/like?id=" + commentId)
}

export async function postComment(form) {
    return service.post("/comment/create", form, {})
}