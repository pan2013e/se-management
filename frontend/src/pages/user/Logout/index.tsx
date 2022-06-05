import React, {useEffect} from "react";

const Logout : React.FC = () => {

    useEffect(() => {
        localStorage.clear();
        window.location.href = '/index';
    }, []);

    return (<div/>);
};

export default Logout;