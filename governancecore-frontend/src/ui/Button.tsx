import "./Button.css"; // Loading the CSS file that styles this Button component.

// The following line means: This Button component accepts the same props that a normal HTML button accepts.
type Props = React.ButtonHTMLAttributes<HTMLButtonElement>;

export default function Button(props: Props) {
  return <button className="ui-button" {...props} />;
}