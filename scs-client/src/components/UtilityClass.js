import axios from "axios";


   export function getAxios(){
       console.log('getAxios')
       let str = localStorage.getItem('scs-properties')
       const jsonstr = JSON.parse(str)
      return axios.create({
          baseURL : jsonstr.scs_coordinator_protocol + "://" + jsonstr.scs_coordinator_host + ":" + jsonstr.scs_coordinator_port,
          responseType : "json",
          headers :{
              "Access-Control-Allow-Credentials" : true
          }
      })
    }
