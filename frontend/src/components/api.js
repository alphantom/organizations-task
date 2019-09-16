import axios from 'axios'

const AXIOS = axios.create({
    baseURL: `/api`,
    timeout: 10000
});


export default {
    organizationList(filters) {
        return AXIOS.post('/organization/list', {
              name:filters.name,
              inn:filters.inn,
              isActive:filters.active,
        });
    },
    organization(id) {
        return AXIOS.get('/organization/'+id);
    },
    organizationSave(organization) {
        return AXIOS.post('/organization/update', {
            name:organization.name,
            fullName:organization.fullname,
            inn: organization.inn,
            kpp: organization.kpp,
            address: organization.address,
            phone: organization.phone,
            isActive: organization.active
        });
    },
    organizationUpdate(organization) {
        return AXIOS.post('/organization/update', {
            id: organization.id,
            name:organization.name,
            fullName:organization.fullname,
            inn: organization.inn,
            kpp: organization.kpp,
            address: organization.address,
            phone: organization.phone,
            isActive: organization.active
        });
    },
}
