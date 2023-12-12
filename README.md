# Command Line TTS
I will keep on accidentally referring to it as CLI TTS - so I just named the repo that to get it out of the way

This is a basic Command Line based Text-To-Speech application.
This application interfaces with Amazon Polly, and requires the use of standard voices.
It has the ability to use [SSML](https://docs.aws.amazon.com/polly/latest/dg/supportedtags.html) (Speech Synthesis
Markup Language), and the configuration has the ability to set up text to replace.

## Replace Text

The replace text section of the configuration allows you to set specific strings that will be replaced by other strings.
This can be very useful for when you want to use SSML and set a shorthand for the beginning tag and ending tag.
This can also be useful for having 'macros'/'commands' with different responses

> **Note**:
> <br>It is advised to only replace specific combinations of characters, and not common combinations/single characters
> <br>For example don't replace `"e"` with `"𝔼"` otherwise the tag `"__"` will break because it will now
> be `<𝔼mphasis l𝔼v𝔼l=\"strong\">` instead of `<emphasis level=\"strong\">`
> <br>You can mitigate this issue by surrounding the letter with a space like this: `" e "`

## Voice Prefixes

The voice prefixes section allows you to set a prefix that will switch the voice to a specific Polly voice.
Then, if you use that prefix at the beginning of a message, the application will switch to that voice until another
prefix is used.

### Default configuration

```HOCON
aws-region = "US_EAST_1"
default-voice = "Kimberly"
replace-text {
  "**" = "<prosody volume=\"x-loud\" pitch=\"low\" rate=\"slow\">"
  "/*" = "</prosody>"
  "*/" = "</prosody>"
  "~~" = "<amazon:effect name=\"whispered\">"
  "/~" = "</amazon:effect>"
  "~/" = "</amazon:effect>"
  "__" = "<emphasis level=\"strong\">"
  "/_" = "</emphasis>"
  "_/" = "</emphasis>"
  "++" = "<prosody volume=\"x-loud\" rate=\"x-fast\" pitch=\"x-high\">"
  "/+" = "</prosody>"
  "+/" = "</prosody>"
  "!!" = "<say-as interpret-as=\"expletive\">"
  "/!" = "</say-as>"
  "!/" = "</say-as>"
  " - " = "<break time=\"300ms\"/>"
  "<3" = "heart emoji"
}
voice-prefixes {
  "R:" = "Salli"
  "E:" = "Kimberly"
  "D-" = "Brian"
}
```

Todo:

- [ ] Make closing tags less annoying
- [x] Manage exceptions better
- [x] Probably unify the config into one file
- [ ] Add configuration for audio output destination
