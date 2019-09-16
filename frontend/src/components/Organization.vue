<template>
    <div class="organization">
        <h1> {{ title }}</h1>

        <input type="text" v-model="organization.name" placeholder="name">
        <input type="text" v-model="organization.fullname" placeholder="fill name">
        <input type="text" v-model="organization.inn" placeholder="inn">
        <input type="text" v-model="organization.kpp" placeholder="kpp">
        <input type="text" v-model="organization.phone" placeholder="phone">
        <input type="text" v-model="organization.address" placeholder="address">
        <input type="checkbox" v-model="organization.active">

        <button @click="save()">Save</button>

        <div v-if="showResponse"><h6>Organization {{ response }}</h6></div>
    </div>
</template>

<script>
    import api from "./api";
    export default {
        props: ['id'],
        name: "Organization",
        data: {
            organization: {
                id: this.id,
                name: null,
                fullname: null,
                inn: null,
                kpp: null,
                address: null,
                phone: null,
                active: false
            },
            response: null,
        },
        computed: {
            isNew() {
                return this.organization.id === null;
            },
            showResponce() {
                return this.showResponce() !== null;
            },
            title() {
                return isNew() ? 'Create organization' : 'Update organization';
            },
        },
        methods: {
            save() {
                if (this.isNew()) {
                    let result = api.organizationSave(this.organization);
                } else {
                    let result = api.organizationUpdate(this.organization);
                }
                result.then(data => {
                    this.response = data.error ? data.error: data.data.result;
                }).catch(error => this.response = error.toString);

            }
        }
    }
</script>

<style scoped>
    h1, h2 {
        font-weight: normal;
    }
    a {
        color: #42b983;
    }
</style>