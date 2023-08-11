import {atom, selector} from "recoil";

export const TokenAtom = atom({
  key: 'TokenAtom',
  default: undefined,
});

export const isLoginSelector = selector({
  key: 'isLoginSelector',
  get: ({get}) => !!get(TokenAtom),
})