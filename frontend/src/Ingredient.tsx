import {BaseIngredient} from "./Model/BaseIngredient.ts";

type IngredientProps={
    ingredient:BaseIngredient[]
}
export default function Ingredient(props:IngredientProps){

    return (
        <>
            {props.ingredient.map((i) => (
                <div key={i.id}>{i.name}</div>
            ))}
        </>
    );
}