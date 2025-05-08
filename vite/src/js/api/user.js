import service from '@/js/utils/request.js'

export function getUserCache (userId) {
    return service.get("/no_auth/user/get_minimal", {
        params: {
            id: userId
        }
    })
}

export function getMemberCache (categoryId, userId) {
    return service.get("/no_auth/category/get_member", {
        params: {
            categoryId: categoryId,
            userId: userId
        }
    })
}

export async function getJoinedCategoryIds(id) {
    return service.get("/no_auth/user/get_joined_category", {
        params: {
            id: id
        }
    })
}

export async function getFullInfo(id) {
    return service.get("/no_auth/user/get_full_info", {
        params: {
            id: id
        }
    })
}

export async function getFollowed(id) {
    return service.get("/user/get_followed", {
        params: {
            id: id
        }
    })
}

export async function getOverallExp(id) {
    return service.get("/no_auth/user/get_overall_exp", {
        params: {
            id: id
        }
    })
}

export async function followUser(id) {
    return service.post("/user/follow?id=" + id)
}

export async function updateUser(nickName, introduce, gender) {
    return service.postForm("/user/update", {
        nickName: nickName,
        introduce: introduce,
        gender: gender
    })
}