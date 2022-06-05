import React, {useEffect} from "react";

const Logout : React.FC = () => {

    useEffect(() => {
        localStorage.clear();
        window.location.href = '/';
    }, []);

    return (<div/>);
};

export default Logout;