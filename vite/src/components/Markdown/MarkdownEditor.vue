<template>
    <MdEditor v-model="model" @on-upload-img="onUploadImage"/>
</template>

<script setup>
import { uploadImage } from '@/js/api/upload';
import { MdEditor } from 'md-editor-v3';
import 'md-editor-v3/lib/style.css';

const model = defineModel()

async function onUploadImage(files, callback) {
    const response = await Promise.all(
        files.map(file => {
            return new Promise((resolve, reject) => {
                uploadImage(file)
                .then(res => resolve(res))
                .catch(err => reject(err))
            })
        })
    )
    callback(response)
}

</script>

<style lang="scss" scoped>

</style>