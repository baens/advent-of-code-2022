use std::collections::HashSet;
use std::iter::FromIterator;
use std::fs;

fn main() {
    let text = fs::read_to_string("../input").expect("Reading file");
    println!("Problem 1 {}", find_signal(&text, 4));
    println!("Problem 2 {}", find_signal(&text, 14));
}

fn find_signal(text:&String, size:usize)->u32 {
    let mut vec = Vec::new();

    for (index, character) in text.chars().into_iter().enumerate() {
        vec.push(character);

        if vec.len() < size {
            continue;
        }

        if HashSet::<&char>::from_iter(&vec).len() == size {
            return index as u32 + 1;
        }

        vec.remove(0);
    }

    return 0;
}


#[cfg(test)]
mod tests {
    use rstest::rstest;
    use super::*;

    #[rstest]
    #[case("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 4, 7)]
    #[case("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 14, 19)]
    #[case("bvwbjplbgvbhsrlpgdmjqwftvncz", 4, 5)]
    #[case("bvwbjplbgvbhsrlpgdmjqwftvncz", 14, 23)]
    #[case("nppdvjthqldpwncqszvftbrmjlhg", 4, 6)]
    #[case("nppdvjthqldpwncqszvftbrmjlhg", 14, 23)]
    #[case("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 4, 10)]
    #[case("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 14, 29)]
    #[case("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 4, 11)]
    #[case("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 14, 26)]
    fn verify_test(#[case] data:String, #[case] size:usize, #[case] expected:u32) {
        let actual = find_signal(&data, size);
        assert_eq!(expected, actual);
    }
}
