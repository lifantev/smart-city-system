import axios from "axios";

export function GetAxios(){
    let str = localStorage.getItem('scs-properties')
    const jsonstr = JSON.parse(str)
    return axios.create({
        baseURL : jsonstr["scs-coordinator-protocol"] + "://" + jsonstr["scs-coordinator-host"] + ":" + jsonstr["scs-coordinator-port"],
        responseType : "json",
        headers :{
            "Access-Control-Allow-Credentials" : true
        }
    })
}