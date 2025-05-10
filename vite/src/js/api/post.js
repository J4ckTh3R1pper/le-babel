import service from "@/js/utils/request.js"

export async function getPostDetails(id) {
    return service.get(
        "/no_auth/post/thread", {
            params: {
                id: id
            }
    })
}

export function getPostSlice(categoryId, userId, keyword, pageNum, pageSize, sort) {
    return service.get(
        "/no_auth/post/get_posts", {
            params: {
                categoryId: categoryId,
                userId: userId,
                page: pageNum,
                keyword: keyword,
                size: pageSize,
                sort: sort
            }
        }
    )
}

export async function getPostMinimalList(ids) {
    return service.get("/no_auth/post/get_minimal_list", {
        params: {
            ids: ids
        },
        paramsSerializer: {
            indexes: null
        }
    })
}

export async function likePost(postId) {
    return service.put("/post/like?id=" + postId)
}

export async function createPost(form) {
    return service.post("/post/create", form, {})
}

export async function deletePost(id) {
    return service.put("/post/delete?id=" + id);
}

export async function searchPost(keyword) {
    return service.get("/no_auth/post/search", {
        params: {
            keyword: keyword
        }
    })
}

export async function getSingle(id) {
    return service.get(
        "/no_auth/post/get_single", {
            params: {
                id: id
            }
    })
}