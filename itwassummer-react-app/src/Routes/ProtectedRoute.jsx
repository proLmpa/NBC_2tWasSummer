import {Navigate, Outlet} from "react-router-dom";
import {useRecoilValue} from "recoil";
import {isLoginSelector} from "../recoil/TokenAtom";

const ProtectedRoute = () => {
  const isLogin = useRecoilValue(isLoginSelector)
  return isLogin ? <Outlet/> : <Navigate to={'/login'} replace={true}/>;
}


export default ProtectedRoute;