import boto3
import pygame
import json
from io import BytesIO

global voice_id
global replace_list
global voice_list


def set_voice(text):
    global voice_id
    for prefix, voice_name in voice_list.items():
        if text.startswith(prefix):
            voice_id = voice_name
            words = text.split()
            text = ' '.join(words[1:])
    return text


def replace_custom_tags(text):
    global replace_list
    for replace_text, tag in replace_list.items():
        text = text.replace(replace_text, tag)
    return f'<speak>{text}</speak>'


def synthesize_speech(text):
    polly_client = boto3.client('polly', region_name='us-east-1')  # Replace 'your_region' with your AWS region

    # Replace custom tags with corresponding SSML tags
    text = replace_custom_tags(text)

    response = polly_client.synthesize_speech(
        Text=text,
        VoiceId=voice_id,
        OutputFormat='mp3',
        TextType='ssml'  # Specify that the input text is in SSML format
    )
    return BytesIO(response['AudioStream'].read())


def play_audio(stream):
    pygame.mixer.music.load(stream)
    pygame.mixer.music.play()
    while pygame.mixer.music.get_busy():
        pygame.time.Clock().tick(10)


def set_replace_and_voice_list():
    global replace_list
    replace_list = {}
    global voice_list
    voice_list = {}
    try:
        with open('text_replace.json', 'r') as replace_list_file:
            replace_list = json.load(replace_list_file)
    except FileNotFoundError:
        print("File 'text_replace' not found")
        exit()
    try:
        with open('voice_prefixes.json', 'r') as voice_list_file:
            voice_list = json.load(voice_list_file)
    except FileNotFoundError:
        print("File 'voice_prefixes' not found")
        exit()


def main():
    pygame.mixer.init()
    # Default voice ID
    global voice_id
    voice_id = 'Kimberly'
    set_replace_and_voice_list()
    print("Type your text, press Enter to convert to speech. Type '--exit' to end the program.")

    while True:
        text = input("Text: ")
        text = set_voice(text)
        if text.lower() == '--exit':
            break
        audio_stream = synthesize_speech(text)
        play_audio(audio_stream)


if __name__ == "__main__":
    main()
