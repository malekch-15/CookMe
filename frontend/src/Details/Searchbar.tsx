import {ChangeEvent} from "react";

import "./Searchbar.css"
type SearchProps={
    onSearch: (query: string) => void;
}

export default function Searchbar(props:SearchProps){
    const handleSearchChange = (event: ChangeEvent<HTMLInputElement>) => {
       props.onSearch(event.target.value);
    };

 return(
        <div className="searchbar-container">
            <input
            type="text"
            placeholder="Search recipe..."
            onChange={handleSearchChange}
            />
        </div>
    )
}
