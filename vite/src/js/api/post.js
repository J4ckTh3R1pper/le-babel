import service from "@/js/utils/request.js"

export function getPostDetails(id) {
    return service.get(
        "/no_auth/post/thread", {
            params: {
                id: id
            }
        })
}

export function getPostSlice(categoryId, pageNum, pageSize, sort) {
    return service.get(
        "/no_auth/post/get_posts", {
            params: {
                categoryId: categoryId,
                page: pageNum,
                size: pageSize,
                sort: sort
            }
        }
    )
}

export function likePost(postId) {
    return service.post("/post/like", {
        id: postId
    })
}