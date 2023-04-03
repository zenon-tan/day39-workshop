import { CComment } from "./Comment"

export interface Character {
    charId: number
    name: string
    imageUrl: string
    comments: CComment[]
}