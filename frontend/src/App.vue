<template>
    <div id="app">
        <h1>Simple Spring Boot App</h1>
        <div id="nav">
            <router-link to="/">App</router-link>
            <router-link to="/organization">Organizations</router-link> |
            <router-link to="/office">Offices</router-link> |
            <router-link to="/user">Users</router-link> |
            <router-link to="/catalog">Catalogs</router-link> |
        </div>
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        name: 'app'
    }
</script>

<style lang="scss">
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        text-align: center;
        color: #2c3e50;
        margin-top: 20px;
    }

    #nav {
        padding: 30px;
        a {
            font-weight: bold;
            color: #2c3e50;
            &.router-link-exact-active {
                color: #42b983;
            }
        }
        h1 {
            color: #3ca776;
        }
    }
</style>