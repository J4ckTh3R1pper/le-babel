import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import {jsx} from "vue/jsx-runtime";
import vueJsxPlugin from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
  esbuild: {
    loader: "jsx"
  },
  resolve: {
    alias:{
      'vue': 'vue/dist/vue.esm-bundler.js'
    },
  },
  plugins: [vue(), vueJsxPlugin()],
  build: {
    outDir: "dist"
  }
})
