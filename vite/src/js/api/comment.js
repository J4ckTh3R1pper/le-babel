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

export async function getSingleComment(commentId) {
    return service.get("/no_auth/comment/get_single", {
        params: {
            id: commentId
        }
    })
}

export async function getCommentDetail(commentId) {
    return service.get("/no_auth/comment/get_detail", {
        params: {
            id: commentId
        }
    })
}

export async function getCommentData(commentId) {
    return service.get("/no_auth/comment/get_data", {
        params: {
            id: commentId
        }
    })
}

export async function likeComment(commentId) {
    return service.put("/comment/like?id=" + commentId)
}

export async function postComment(form) {
    return service.post("/comment/create", form, {})
}