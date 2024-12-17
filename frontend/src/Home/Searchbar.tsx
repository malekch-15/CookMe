import {ChangeEvent} from "react";

import "../css/Searchbar.css"
type SearchProps={
    onSearch: (query: string) => void;
}

export default function Searchbar(props:Readonly<SearchProps>){
    const handleSearchChange = (event: ChangeEvent<HTMLInputElement>) => {
       props.onSearch(event.target.value);
    };

 return(
      <>
            <input
                className="searchbar-container-input"
            type="text"
            placeholder="Search recipe..."
            onChange={handleSearchChange}
            />
      </>
    )
}
